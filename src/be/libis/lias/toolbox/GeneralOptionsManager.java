package be.libis.lias.toolbox;

import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import uk.co.flamingpenguin.jewel.cli.ArgumentValidationException;
import uk.co.flamingpenguin.jewel.cli.CliFactory;

/**
 * 
 * This class deals with the default actions required to parse the options.
 * <p>
 * 
 * It also processes the general options specified in GeneralOptions.
 * 
 * See processOptions() for details on usage.
 * 
 * @param <T>
 *          Specify an interface that inherits from GeneralOptions and which
 *          contains the application specific options.
 * 
 * 
 */
public class GeneralOptionsManager<T> {

  private Handler console_handler = new ConsoleLogger();

  /**
   * Sends the exception info to the logger
   * 
   * @param e
   *          The exception to log
   * @param logger
   *          The Logger object that will receive the entry
   */
  public static void printExceptionInfo(Exception e, Logger logger) {
    logger.severe(e.getClass().getName() + " : " + e.getMessage());
    for (StackTraceElement el : e.getStackTrace()) {
      logger.finer(" - " + el.toString());
    }
  }

  /**
   * Sets the logger to its default state.
   * 
   * The default state is to log INFO level messages only to the console. A
   * {@link ConsoleLogger} is added to the logger.
   * 
   * @param logger
   */
  private void initializeLogger(Logger logger) {
    console_handler.setLevel(Level.INFO);
    logger.setLevel(Level.ALL);
    logger.addHandler(console_handler);
    logger.setUseParentHandlers(false);
  }

  /**
   * Processes all the command line options that deal with configuring the
   * logger.
   * 
   * The options processed are:
   * <ul>
   * <li>Logging file
   * <li>File logging level
   * <li>Console logging level
   * </ul>
   * 
   * @param options
   *          The object containing the parsed command line options
   * @param logger
   *          The logging to be configured
   * @return true if successful , false otherwise.
   */
  private boolean processLoggingOptions(GeneralOptions options, Logger logger) {

    if (options.isLogFile()) {
      Level level;
      level = intToLevel(options.getLogLevel());
      if (level == null) {
        logger.severe("Bad logging level: " + options.getLogLevel());
        return false;
      }
      String filename = options.getLogFile();
      Handler filehandler;
      try {
        filehandler = new FileLogger(filename);
      } catch (IOException ex) {
        logger.severe("Could not log to file '" + filename + "' : " + ex.getMessage());
        return false;
      }
      filehandler.setLevel(level);
      logger.addHandler(filehandler);
    }

    if (options.isConsoleLogLevel()) {
      Level level = intToLevel(options.getConsoleLogLevel());
      if (level == null) {
        logger.severe("Bad logging level: " + options.getConsoleLogLevel());
        return false;
      }
      console_handler.setLevel(level);
    }

    return true;
  }

  /**
   * The method that parses the command line options and deals with the general
   * options.
   * 
   * <p>
   * In your application's main, call this method like this (with <i>T</i> your
   * derived {@link GeneralOptions} interface) :
   * <p>
   * <code>
   * <i>T</i> options = new GeneralOptionsManager<<i>T</i>>().processOptions(<i>T</i>.class, args, logger);<br>
   * if (options == null) return; // error messages are printed by GeneralOptionsHandler
     * </code>
   * <p>
   * You can then query the resulting option object for your command line
   * options.
   * 
   * @param c
   *          Class of the derived interface. This is typically <i>T.class</i>
   * @param args
   *          Pass the command line arguments here.
   * @param logger
   *          Pass the logger object you created for your application.
   * @return a <i>T</i> object or null if parsing failed.
   */
  public T processOptions(Class<T> c, String[] args, Logger logger) {

    initializeLogger(logger);

    T options = null;

    try {

      options = CliFactory.parseArguments(c, args);

      if (options == null || false == processLoggingOptions((GeneralOptions) options, logger)) {
        System.err.println();
        System.err.println(CliFactory.createCli(c).getHelpMessage());
        System.err.println();
        return null;
      }

    } catch (ArgumentValidationException ex) {
      System.err.println();
      System.err.println(ex.getMessage());
      if (!ex.getMessage().startsWith("Usage:")) {
        System.err.println();
        System.err.println(CliFactory.createCli(c).getHelpMessage());
      }
      System.err.println();
      return null;
    } catch (Exception e) {
      printExceptionInfo(e, logger);
      return null;
    }

    return options;
  }

  /**
   * Converts an integer to a {@link Level} object.
   * 
   * <ul>
   * <li>0 = OFF
   * <li>1 = SEVERE
   * <li>2 = WARNING
   * <li>3 = INFO
   * <li>4 = ALL
   * </ul>
   * 
   * @param i
   *          Logging level number.
   * @return The generated Level object or null if the number was out of range.
   */
  public static Level intToLevel(int i) {
    Level level = null;
    switch (i) {
      case 0:
        level = Level.OFF;
        break;
      case 1:
        level = Level.SEVERE;
        break;
      case 2:
        level = Level.WARNING;
        break;
      case 3:
        level = Level.INFO;
        break;
      case 4:
        level = Level.ALL;
        break;
    }
    return level;
  }
}
