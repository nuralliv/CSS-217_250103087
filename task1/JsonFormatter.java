package task1;

public class JsonFormatter implements ReportFormatter {
    @Override
    public String format(String title, String[] headers, String[][] rows) {
        if (headers == null || rows == null) {
            throw new IllegalArgumentException("Headers and rows cannot be null");
        }

        StringBuilder json = new StringBuilder();
        json.append("{\n  \"report\": \"").append(escape(title)).append("\",\n");
        json.append("  \"records\": [");

        for (int i = 0; i < rows.length; i++) {
            if (rows[i] == null || rows[i].length != headers.length) {
                throw new IllegalArgumentException("Every row must match the number of headers");
            }
            json.append(i == 0 ? "\n    {" : ",\n    {");
            for (int j = 0; j < headers.length; j++) {
                if (j > 0) json.append(", ");
                json.append("\"").append(escape(headers[j])).append("\": \"")
                     .append(escape(rows[i][j])).append("\"");
            }
            json.append("}");
        }

        if (rows.length > 0) json.append("\n  ");
        json.append("]\n}");
        return json.toString();
    }

    private String escape(String value) {
        if (value == null) return "";
        return value.replace("\\\\", "\\\\\\\\")
                    .replace("\"", "\\\\\"")
                    .replace("\\b", "\\\\b")
                    .replace("\\f", "\\\\f")
                    .replace("\\n", "\\\\n")
                    .replace("\\r", "\\\\r")
                    .replace("\\t", "\\\\t");
    }
}
