Design Patterns Applied Lab — Tasks 1 and 2
============================================

Requirements:
- Java SE 8 or newer
- Standard Java library only (no Maven/Gradle or external dependencies)

Project layout:
  task1/  Strategy + Decorator (report formatting and secure export)
  task2/  Adapter + Proxy (legacy stock feed and TTL price cache)

Compile from the folder containing the task1 and task2 directories:

  javac task1/*.java task2/*.java

Run Task 1:
  java task1.Task1Runner

Run Task 2:
  java task2.Task2Runner

Task 1 notes:
- JsonFormatter creates escaped, valid JSON.
- CsvFormatter quotes fields and escapes embedded quotes.
- Exporter decorators can be stacked in any order.
- HeaderFooterDecorator adds the timestamp, record count, and integrity marker.
- Base64EncryptDecorator encodes the complete decorated payload using UTF-8.
  (Base64 is encoding, not cryptographic encryption.)

Task 2 notes:
- StockFeedAdapter converts the semicolon-separated legacy packet to StockProvider.
- Market price is the midpoint: (BID + ASK) / 2.
- CachedStockProxy caches market prices for the configured TTL.
- getStatus delegates to the adapter; the assignment specifies price caching.
