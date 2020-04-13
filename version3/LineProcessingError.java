public class LineProcessingError extends RuntimeException{
    private String line;
    public LineProcessingError(Exception ex, String line) {
        super(ex.getClass() + " encountered while processing line: ");
        this.line = line;
    }
    public String getLine() {
        return line;
    }
}
