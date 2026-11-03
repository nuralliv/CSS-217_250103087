package task1;

public class CsvFormatter implements ReportFormatter {
    @Override
    public String format(String title, String[] headers, String[][] rows) {
        if (headers == null || rows == null) {
            throw new IllegalArgumentException("Headers and rows cannot be null");
        }

        StringBuilder csv = new StringBuilder();
        csv.append("# TITLE: ").append(quote(title)).append("\n");

        for (int i = 0; i < headers.length; i++) {
            if (i > 0) csv.append(",");
            csv.append(quote(headers[i]));
        }
        csv.append("\n");

        for (String[] row : rows) {
            if (row == null || row.length != headers.length) {
                throw new IllegalArgumentException("Every row must match the number of headers");
            }
            for (int i = 0; i < row.length; i++) {
                if (i > 0) csv.append(",");
                csv.append(quote(row[i]));
            }
            csv.append("\n");
        }
        return csv.toString();
    }

    private String quote(String value) {
        String safe = value == null ? "" : value;
        return "\"" + safe.replace("\"", "\"\"") + "\"";
    }
}
