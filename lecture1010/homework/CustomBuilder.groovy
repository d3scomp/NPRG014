import javax.swing.*
import java.awt.*

//TASK The MarkupBuilder in Groovy can transform a hierarchy of method calls and nested closures into a valid XML document.
//Create a VisualHierarchyBuilder builder that will transform a user-specified hierarchy of any sort into a visualized hierarchy using Swing components.
//This script must return an instance of JPanel containing visual boxes representing each element of the provided hierarchy.
//The boxes must be organized visually into a hierarchy that reflects the original hierarchy expressed by the supplied code.
//Change or add to the code in the script. Reuse the infrastructure code at the bottom of the script.

class VisualHierarchyBuilder extends BuilderSupport {
    final static int EXPECTED_BOX_SIZE = 50
    final static int WIDTH = 1800
    final static int HEIGHT = 900
    //TODO implement fields and methods as you feel fit
    
    protected void setParent(Object parent, Object child) {
        parent.children << child
    }

    protected Object createNode(Object nodeName) {
        createNode nodeName, null, null
    }

    protected Object createNode(Object nodeName, Object value) {
        createNode nodeName, null, value
    }

    protected Object createNode(Object nodeName, Map attrs) {
        createNode nodeName, attrs, null
    }

    protected Object createNode(Object nodeName, Map attrs, Object value) {
        //TODO implement
    }
    
    public JPanel toJPanel() {
        //todo implement
    }
}

//JButton is the easiest component to extend, but you may use any other swing component, if you like
class Item extends JButton {

    //TODO implement fields and methods as you find appropriate

    @Override
    public void paintComponent(Graphics g) {
        //paintComponent is the right method to override if you want to customize visual appearance of a Swing component
        //TODO implement
        super.paintComponent(g)
    }
}


//------------------------- Do not modify beyond this point!

//Calls the supplied builder on the supplied script
def build(builder, String specification) {
    def binding = new Binding()
    binding['builder'] = builder
    new GroovyShell(binding).evaluate(specification)
    return builder
}

//Custom hierarchy to display
String description = '''
builder.animals {
    birds {
        singers {
            parrot(discoverredIn: 1679, extinct: false)
            nightingale()
        }
    }
    mamals {
        apes {
            primates {
                gorilla(discoverredIn: 1387, extinct: false)
                chimp(extinct: false)
            }
            baboon(extinct: false)
        }
        dogs {
            husky(extinct: false)
            bandog()
        }
    }
}
'''

//XML builder building an XML document
def xml = build(new groovy.xml.MarkupBuilder(), description)
println xml.toString()

//VisualHierarchyBuilder displaying the hierarchy in a JFrame
def hierarchy = build(new VisualHierarchyBuilder(), description)
JPanel panel = hierarchy.toJPanel()

def frame = new groovy.swing.SwingBuilder().frame(title: "Hierarchy", preferredSize: new Dimension(VisualHierarchyBuilder.WIDTH, VisualHierarchyBuilder.HEIGHT))
frame.add(panel)
frame.setVisible(true)
frame.pack()