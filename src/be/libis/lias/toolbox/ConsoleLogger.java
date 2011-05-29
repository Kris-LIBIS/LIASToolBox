package be.libis.lias.toolbox;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/** A Logger implementation that sends log info to the console. */
public class ConsoleLogger extends ConsoleHandler {

  public ConsoleLogger() {
  }

  /** This method is called when a log record needs to be printed. */
  @Override
  public void publish(LogRecord record) {
    int level = record.getLevel().intValue();
    if (level < getLevel().intValue()) {
      return;
    }
    String levelTxt = level < Level.INFO.intValue() ? "DEBUG" : level <= Level.INFO.intValue() ? "INFO"
        : level <= Level.WARNING.intValue() ? "WARNING" : "ERROR";

    System.err.printf("%7s - %s\n", levelTxt, record.getMessage());
    flush();
  }

  /** This method is called when the logger is requested to flush its output. */
  @Override
  public void flush() {
    super.flush();
  }

  /** This method is called when the logger is requested to close its output. */
  @Override
  public void close() {
    super.close();
  }
}
