class IndentBuilder extends BuilderSupport {
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
        final node = new Item(nodeName)
        if (value) node.value = value
        return node
    }
}

class Item {
    final String name
    final List children = []
    String value

    public Item(String name) {
        this.name = name
    }

    @Override
    String toString() {
        buildString(0)
    }

    String buildString(indent) {
        "\n${' ' * indent}<$name>" + (value ? "\n${' ' * (indent + 1)}$value" : children.collect {it.buildString(indent + 1)}.join()) + "\n${' ' * indent}</$name>"
    }
}

final builder = new IndentBuilder()
println(builder.html {
    head {
        title 'Demo'
    }
    body {
        div {
            h1 'hello'
        }
        p(align: "Center", color: "Magenta", "This is my document")    }
})

//TASK Add support for html attributes, as used above in p(align: "Center", color: "Magenta")