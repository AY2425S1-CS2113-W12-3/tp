package wheresmymoney.exception;
public class WheresMyMoneyException extends RuntimeException {
    /**
     * Creates WheresMyMoneyException
     * @param errorMessage Error Message
     */
    public WheresMyMoneyException(String errorMessage) {
        super(errorMessage);
    }
}
