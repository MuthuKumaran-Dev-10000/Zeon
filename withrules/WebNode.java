package withrules;

public class WebNode extends Node {
    private final String htmlContent;

    public WebNode(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public String getHtmlContent() {
        return htmlContent;
    }
}
