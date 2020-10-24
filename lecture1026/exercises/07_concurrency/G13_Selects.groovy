import groovyx.gpars.dataflow.DataflowVariable
import groovyx.gpars.dataflow.DataflowQueue
import groovyx.gpars.dataflow.DataflowBroadcast
import groovyx.gpars.group.NonDaemonPGroup
import groovyx.gpars.group.PGroup

import javax.swing.JFrame
import javax.swing.JSlider
import javax.swing.JLabel
import java.awt.BorderLayout
import javax.swing.SwingConstants
import javax.swing.SwingUtilities
import groovy.swing.SwingBuilder


def message = new JLabel('Status')
def slider = new JSlider(SwingConstants.HORIZONTAL, 0, 10, 0)
buildUI(message, slider)


def group = new NonDaemonPGroup()
def progressNotifications = new DataflowQueue()
def labelUpdates = new DataflowQueue()
def exit = new DataflowVariable()

group.task {
    10.times {
        sleep 1000
        progressNotifications << 1
    }
    exit << true
}

group.task {
        labelUpdates << 'Downloading stuff'
        sleep 3000
        labelUpdates << 'Installing'
        sleep 3000
        labelUpdates << 'Finishing'                
        sleep 4000
        labelUpdates << 'Done'                
}


//Handle progress
group.task {
    def alt = group.select(progressNotifications, exit)
    while(true) {
        def msg = alt.select()
        if (msg.index == 1) break
        SwingUtilities.invokeLater{slider.value = slider.value + msg.value}
    }
}

//Handle Message updates
group.task {
    def alt = group.select(labelUpdates, exit)
    while(true) {
        def msg = alt.select()
        if (msg.index == 1) break
        SwingUtilities.invokeLater{message.text = msg.value}
    }
}.join()

//TASK Merge the two consumer process into a single one




def buildUI(JLabel myLabel, JSlider mySlider) {
final JFrame frame = new SwingBuilder().frame(title: 'Demo') {
}
frame.add(myLabel, BorderLayout.NORTH)
frame.add(mySlider, BorderLayout.CENTER)
frame.visible = true
frame.pack()
}