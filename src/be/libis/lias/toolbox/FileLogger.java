package be.libis.lias.toolbox;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/** A Logger implementation that sends log info to a file. */
public class FileLogger extends FileHandler {

  PrintWriter file;

  /**
   * Opens the file and prepares it for printing.
   * 
   * @param filename
   *          The path of the file to log to.
   * @throws IOException
   */
  public FileLogger(String filename) throws IOException {
    this.file = new PrintWriter(new FileOutputStream(filename));
  }

  /** This method is called when the logger is destroyed. */
  @Override
  protected void finalize() throws Throwable {
    super.finalize();
    file.close();
  }

  /** This method is called when a log record needs to be printed. */
  @Override
  public void publish(LogRecord record) {
    int level = record.getLevel().intValue();
    if (level < getLevel().intValue()) {
      return;
    }
    String levelTxt = level < Level.INFO.intValue() ? "D" : level <= Level.INFO.intValue() ? "I"
        : level <= Level.WARNING.intValue() ? "W" : "E";

    file.printf("%s,%d,[%6$tY-%6$tm-%6$td %6$tH:%6$tM:%6$tS.%6$tL],%s,%s,%s\n", levelTxt, record.getSequenceNumber(),
        record.getSourceClassName(), record.getSourceMethodName(), record.getMessage(), record.getMillis());
    flush();
  }

  /** This method is called when the logger is requested to flush its output. */
  @Override
  public void flush() {
    file.flush();
    super.flush();
  }

  /** This method is called when the logger is requested to close its output. */
  @Override
  public void close() throws SecurityException {
    file.close();
    super.close();
  }
}
