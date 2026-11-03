package task2;

import java.util.HashMap;
import java.util.Map;

public class StockFeedAdapter implements StockProvider {
    private final LegacyStockFeed legacyFeed;

    public StockFeedAdapter(LegacyStockFeed legacyFeed) {
        if (legacyFeed == null) {
            throw new IllegalArgumentException("Legacy feed cannot be null");
        }
        this.legacyFeed = legacyFeed;
    }

    private Map<String, String> parsePacket(String rawPacket) {
        Map<String, String> values = new HashMap<String, String>();
        if (rawPacket == null || rawPacket.trim().isEmpty()) {
            return values;
        }

        String[] tokens = rawPacket.split(";");
        for (String token : tokens) {
            String[] pair = token.split("=", 2);
            if (pair.length == 2) {
                values.put(pair[0].trim(), pair[1].trim());
            }
        }
        return values;
    }

    @Override
    public double getMarketPrice(String ticker) {
        Map<String, String> packet = parsePacket(legacyFeed.fetchRawTickerPacket(ticker));
        double bid = parsePrice(packet.get("BID"));
        double ask = parsePrice(packet.get("ASK"));
        return (bid + ask) / 2.0;
    }

    @Override
    public String getStatus(String ticker) {
        Map<String, String> packet = parsePacket(legacyFeed.fetchRawTickerPacket(ticker));
        String status = packet.get("STATUS");
        return status == null ? "UNAVAILABLE" : status;
    }

    private double parsePrice(String value) {
        if (value == null) return 0.0;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid price in legacy stock packet: " + value, exception);
        }
    }
}
