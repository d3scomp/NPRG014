import groovy.swing.SwingBuilder

import javax.swing.*

final JFrame frame = new SwingBuilder().frame(title: 'Demo') {
    menuBar {
        menu 'File', {
            menuItem 'Open', actionPerformed: {println 'Opening'}
            menuItem 'Save', actionPerformed: {println 'Saving'}
        }
        menu 'Help', {
            menuItem 'About', actionPerformed: {println 'About us'}
            menuItem 'Clear', actionPerformed: {messages.text = ''}
        }
    }
    vbox() {
        label('Demo')
        scrollPane() {
            textArea(id: 'messages', columns: 50, rows: 20)
        }
        hbox {
            ['Java', 'Groovy'].each {lang -> button(text: "Click $lang", actionPerformed: {messages.text += "Clicked $lang\n"})}
        }
    }
}

frame.visible = true
frame.pack()

//TASK Add a new button for Scala, which when clicked clears the text area and prints out 'Scala'