package com.springnote.api.utils.markdown;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MarkdownConvertor {
    //https://simplesolution.dev/java-convert-markdown-to-html-using-flexmark-java/

    public String convertToHtml(String markdown) {
        var options = new MutableDataSet();
        var parser = Parser.builder(options).build();
        var renderer = HtmlRenderer.builder(options).build();
        var document = parser.parse(markdown);

        return renderer.render(document);
    }

    
    public String convertToPlainText(String text, boolean isHtml){
        if(!isHtml){
            text = convertToHtml(text);
        }
        return Jsoup.parse(text).text();
    }
}
