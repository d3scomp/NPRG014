import groovy.swing.SwingBuilder
import javax.swing.*
import java.awt.*

/**
 * Minimal SwingBuilder-like DSL using BuilderSupport
 */
class SwingDslBuilder extends BuilderSupport {

    protected void setParent(Object parent, Object child) {
        if (parent instanceof Container && child instanceof Component) {
            parent.add(child)
        }
    }

    protected Object createNode(Object name) {
        createNode(name, [:], null)
    }

    protected Object createNode(Object name, Object value) {
        createNode(name, [:], value)
    }

    protected Object createNode(Object name, Map attrs) {
        createNode(name, attrs, null)
    }

    protected Object createNode(Object name, Map attrs, Object value) {
        switch(name) {
            case "panel":
                return new JPanel(new FlowLayout())
            case "label":
                return new JLabel(attrs.text ?: value ?: "")
            case "textField":
                return new JTextField(attrs.text ?: value ?: "", attrs.columns ?: 10)
            case "textArea":
                return new JTextArea(attrs.text ?: value ?: "", attrs.rows ?: 5, attrs.columns ?: 20)
            case "button":
                JButton b = new JButton(attrs.text ?: value ?: "")
                if (attrs.action instanceof Closure) {
                    b.addActionListener { attrs.action.call() }
                }
                return b
            default:
                throw new IllegalArgumentException("Unknown node: $name")
        }
    }
}

// --- Example usage ---
def builder = new SwingDslBuilder()
def ui = builder.panel {
    label(text: "Enter your name:")
    textField(id: "nameField", columns: 15)
    button(text: "Say Hello", action: {
        JOptionPane.showMessageDialog(null, "Hello, world!")
    })
    textArea(text: "Logs...\n", rows: 5, columns: 30)
}

// Show in JFrame
def frame = new JFrame("Groovy Swing DSL Demo")
//frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
frame.contentPane.add(ui)
frame.pack()
frame.visible = true