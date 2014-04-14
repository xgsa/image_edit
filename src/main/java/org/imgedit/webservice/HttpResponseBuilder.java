package org.imgedit.webservice;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.util.CharsetUtil;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;


public class HttpResponseBuilder {
    final private HttpResponse result;

    public HttpResponseBuilder(HttpResponseStatus status) {
        result = new DefaultHttpResponse(HTTP_1_1, status);
    }

    public static HttpResponseBuilder makeOk() {
        return make(HttpResponseStatus.OK);
    }

    public static HttpResponseBuilder make(HttpResponseStatus status) {
        return new HttpResponseBuilder(status);
    }

    private HttpResponseBuilder withContent(ChannelBuffer content) {
        result.setContent(content);
        result.headers().set(CONTENT_LENGTH, content.readableBytes());
        return this;
    }

    public HttpResponseBuilder withContent(byte[] htmlContent) {
        return withContent(ChannelBuffers.wrappedBuffer(htmlContent));
    }

    private HttpResponseBuilder withHtmlContent(ChannelBuffer content) {
        result.headers().set(CONTENT_TYPE, "text/html");
        return withContent(content);
    }

    public HttpResponseBuilder withHtml(String htmlContent, Object... args) {
        return withHtmlContent(ChannelBuffers.copiedBuffer(String.format(htmlContent, args), CharsetUtil.UTF_8));
    }

    public HttpResponseBuilder withHtml(byte[] htmlContent) {
        return withHtmlContent(ChannelBuffers.wrappedBuffer(htmlContent));
    }

    public HttpResponseBuilder withErrorContent(String details) {
        HttpResponseStatus status = result.getStatus();
        return withHtml(
                "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">" +
                        "<html><head>" +
                        "<title>%s</title>" +
                        "</head><body>" +
                        "<h1>%s</h1>" +
                        "<p>%s</p>" +
                        "<hr>" +
                        "Return to the <a href=\"/\">main page</a>" +
                        "</body></html>",
                status.toString(), status.toString(), details
        );
    }

    public HttpResponseBuilder withErrorContent(Throwable t) {
        StringBuilder sb = new StringBuilder();
        sb.append(t.getMessage());
        sb.append("<BR><BR><BR>Stack trace:<pre>");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        sb.append(sw.toString());
        sb.append("</pre>");
        return withErrorContent(sb.toString());
    }

    public HttpResponse build() {

        return result;
    }

}
