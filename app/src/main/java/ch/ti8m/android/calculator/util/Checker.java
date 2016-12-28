package ch.ti8m.android.calculator.util;

/**
 * Utility class to perform checks and throws exceptions if the checks fail.
 *
 * @author Cosimo Damiano Prete
 * @since 28/12/2016
 */

public class Checker {
    /**
     * Checks if a variable is not null.
     * @param param the variable to check
     * @param paramName the name (if any) of the variable
     * @param <T> the type of the variable
     * @throws NullPointerException if param is null
     */
    public static <T> void notNull(T param, String paramName) {
        if(param == null) {
            throw new NullPointerException("The value [" + paramName + "] can't be null.");
        }
    }
}
