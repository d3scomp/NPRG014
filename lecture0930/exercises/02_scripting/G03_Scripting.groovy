import groovy.swing.SwingBuilder

import javax.swing.*
import java.awt.*

SwingBuilder builder = new SwingBuilder()
def frame
def customPanel
frame = builder.frame(title: 'Groovy scripting') {
    vbox {
        def slider = slider(id: 'slider', value: 50)
        def label = label(text: bind(source: slider, sourceProperty: 'value', converter: {"Current value: $it"}))
        scrollPane {
            def codePane = textArea(id: 'codePane', columns: 60, rows: 20)
            setFontSize(codePane)
        }
        hbox {
            customPanel = panel() {
                button('Run', actionPerformed: {
                    doOutside {
                        new GroovyShell().run(codePane.text, "Scripting", [])
                    }
                })

                button('Evaluate in context', actionPerformed: {
                    evaluateScript(codePane, frame, slider, label, customPanel)
                })

                button('Implement Runnable', actionPerformed: {
                    doOutside {
                        def classDefinition = new GroovyShell().evaluate(codePane.text)
                        Runnable task = classDefinition.newInstance()
                        new Thread(task).start()
                    }
                })
            }
        }
    }
}

private def setFontSize(JTextArea codePane) {
    Font currentFont = codePane.font
    codePane.setFont new Font(currentFont.name, currentFont.style, 18)
}

private def evaluateScript(codePane, frame, slider, label, customPanel) {
    def binding = new Binding()
    binding["myFrame"] = frame
    binding["mySlider"] = slider
    binding["myLabel"] = label
    binding["customPanel"] = customPanel
    binding["addButton"] = {name, code ->
        final button = new SwingBuilder().button(name, actionPerformed: code)
        customPanel.add(button)
        customPanel.validate()
        customPanel.repaint()
    }

    def result = ''

    GroovyShell shell = new GroovyShell(binding)
    result = shell.evaluate(codePane.text)

//    codePane.text = "$result"
}
// try addButton('Press me') {def v = mySlider.value;mySlider.value = 0;addButton('Undo', {mySlider.value = v})}addButton('Press me') {def v = mySlider.value;mySlider.value = 0;addButton('Undo', {mySlider.value = v})}
frame.pack()
frame.show()