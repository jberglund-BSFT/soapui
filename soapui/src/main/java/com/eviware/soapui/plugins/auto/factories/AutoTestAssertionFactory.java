/*
 * SoapUI, Copyright (C) 2004-2018 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon as they will be approved by the European Commission - subsequent 
 * versions of the EUPL (the "Licence"); 
 * You may not use this work except in compliance with the Licence. 
 * You may obtain a copy of the Licence at: 
 * 
 * http://ec.europa.eu/idabc/eupl 
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is 
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the Licence for the specific language governing permissions and limitations 
 * under the Licence. 
 */

package com.eviware.soapui.plugins.auto.factories;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.impl.wsdl.panels.assertions.AssertionListEntry;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlMessageAssertion;
import com.eviware.soapui.impl.wsdl.teststeps.assertions.AbstractTestAssertionFactory;
import com.eviware.soapui.model.testsuite.Assertable;
import com.eviware.soapui.plugins.SoapUIFactory;
import com.eviware.soapui.plugins.auto.PluginTestAssertion;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by ole on 15/06/14.
 */
public class AutoTestAssertionFactory extends AbstractTestAssertionFactory implements SoapUIFactory {
    private final String name;
    private final String description;
    private Class<WsdlMessageAssertion> testAssertionClass;
    private String category;

    public AutoTestAssertionFactory(PluginTestAssertion annotation, Class<WsdlMessageAssertion> testAssertionClass) {
        super(annotation.id(), annotation.label(), testAssertionClass);
        this.testAssertionClass = testAssertionClass;
        category = annotation.category();
        name = annotation.label();
        description = annotation.description();
    }

    @Override
    public Class<? extends WsdlMessageAssertion> getAssertionClassType() {
        return testAssertionClass;
    }

    @Override
    public AssertionListEntry getAssertionListEntry() {
        return new AssertionListEntry(getAssertionId(), getAssertionLabel(), description);
    }

    @Override
    public String getCategory() {
        return category;
    }
    
    @Override
    public boolean canAssert(Assertable assertable) {
        try {
            Method method = testAssertionClass.getMethod("canAssert", Assertable.class);
            return (boolean) method.invoke(null, assertable);
        } catch (NoSuchMethodException ignore) {
            
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            SoapUI.logError(ex);
        }
        return super.canAssert(assertable);
    }    
}
