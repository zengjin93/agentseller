/**
 * 
 */
package com.baidu.agentseller.service.integration.rest.client;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;

/**
 * @author liuyanjiang
 *
 */
public class CustomAllEncompassingFormHttpMessageConverter extends FormHttpMessageConverter {

    private String charset = "UTF-8";

    @SuppressWarnings("rawtypes")
    public CustomAllEncompassingFormHttpMessageConverter(String charset) {
        if (!Charset.isSupported(charset)) {
            throw new IllegalCharsetNameException("The given charset " + charset + " is not supported!!");
        }
        addPartConverter(new SourceHttpMessageConverter());
        addPartConverter(new CustomMappingJackson2HttpMessageConverter(charset));
        super.setCharset(Charset.forName(charset));
        this.charset = charset;
    }

    /**
     * @return the charset
     */
    public String getCharset() {
        return charset;
    }
}
