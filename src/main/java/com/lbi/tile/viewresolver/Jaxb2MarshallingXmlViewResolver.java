package com.lbi.tile.viewresolver;

import org.springframework.oxm.Marshaller;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.xml.MarshallingView;

import java.util.Locale;

public class Jaxb2MarshallingXmlViewResolver implements ViewResolver {

	private Marshaller marshaller;

    
    public Jaxb2MarshallingXmlViewResolver(Marshaller marshaller) {
        this.marshaller = marshaller;
    }
    
    
    //@Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        MarshallingView view = new MarshallingView();
        view.setMarshaller(marshaller);
        return view;
    }

}
