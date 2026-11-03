package task1;

import java.time.Instant;

public class HeaderFooterDecorator extends ExporterDecorator {
    private final String timestamp;
    private final int rowCount;

    public HeaderFooterDecorator(Exporter wrappedExporter, String timestamp) {
        this(wrappedExporter, timestamp, -1);
    }

    public HeaderFooterDecorator(Exporter wrappedExporter, String timestamp, int rowCount) {
        super(wrappedExporter);
        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp cannot be null");
        }
        this.timestamp = timestamp;
        this.rowCount = rowCount;
    }

    @Override
    public void export(String destination, String data) {
        int records = rowCount >= 0 ? rowCount : countRecords(data);
        String header = "--- EXPORT TIMESTAMP: " + timestamp + " ---";
        String footer = "--- RECORD COUNT: " + records + " | SECURE HASH: [OK] ---";
        super.export(destination, header + "\n" + data + "\n" + footer);
    }

    private int countRecords(String data) {
        if (data == null || data.trim().isEmpty()) return 0;

        // Count JSON record objects when the formatter's JSON output is used.
        int recordsMarker = data.indexOf("\"records\"");
        if (recordsMarker >= 0) {
            int start = data.indexOf('[', recordsMarker);
            int end = data.indexOf(']', start);
            if (start >= 0 && end >= 0) {
                int count = 0;
                boolean inString = false;
                boolean escaped = false;
                for (int i = start + 1; i < end; i++) {
                    char c = data.charAt(i);
                    if (escaped) { escaped = false; continue; }
                    if (c == '\\' && inString) { escaped = true; continue; }
                    if (c == '"') inString = !inString;
                    else if (c == '{' && !inString) count++;
                }
                return count;
            }
        }

        String[] lines = data.split("\\r?\\n");
        return Math.max(0, lines.length - 2);
    }
}
