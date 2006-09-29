/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
/**
 * @author Anton Avtamonov
 * @version $Revision$
 */
package javax.swing.plaf.metal;

import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.SwingTestCase;

public class MetalComboBoxEditorTest extends SwingTestCase {
    private MetalComboBoxEditor editor;

    public MetalComboBoxEditorTest(final String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        editor = new MetalComboBoxEditor();
    }

    protected void tearDown() throws Exception {
        editor = null;
    }

    public void testMetalComboBoxEditor() throws Exception {
        assertNotNull(MetalComboBoxEditor.editorBorderInsets);

        JTextField textEditor = (JTextField)editor.getEditorComponent();
        assertEquals(MetalComboBoxEditor.editorBorderInsets, textEditor.getInsets());

        MetalComboBoxEditor.editorBorderInsets = new Insets(2, 2, 2, 2);
        assertEquals(MetalComboBoxEditor.editorBorderInsets, textEditor.getInsets());

        textEditor.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        assertNotSame(MetalComboBoxEditor.editorBorderInsets, textEditor.getInsets());

        MetalComboBoxEditor.editorBorderInsets = new Insets(2, 2, 2, 2);
        assertNotSame(MetalComboBoxEditor.editorBorderInsets, textEditor.getInsets());
    }
}
