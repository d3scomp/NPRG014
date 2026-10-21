import java.awt.BorderLayout as BL

import groovy.swing.SwingBuilder
import java.awt.Color
import javax.swing.BorderFactory
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.RecursiveTask

// Shared static state holder for access from static methods and inner classes
class S {
    static final ROW_HEIGHT = 40
    static final COL_WIDTH = 35
    static final COLOR_WAIT = new Color(212, 146, 52)
    static final COLOR_SCHEDULED = new Color(134, 219, 52)
    static final COLOR_FINISHED = Color.GRAY
    static final swing = new SwingBuilder()
    static final threadColors = [Color.YELLOW, new Color(76, 160, 255), Color.CYAN, Color.MAGENTA, Color.GREEN, Color.PINK, Color.ORANGE, Color.WHITE]
    static final panel = new JPanel()
    static speed
    static numThreads
    static problemSize
    static startButton

    /**
     * Creates label that visualizes the task
     */
    static def createLabel(row, col, nums) {
        final label = swing.label(
                text: " " + nums,
                bounds: [col * COL_WIDTH, row * ROW_HEIGHT + 20, nums.size() * COL_WIDTH, ROW_HEIGHT],
                background: COLOR_SCHEDULED,
                opaque: true,
                toolTipText: nums.toString(),
                border: BorderFactory.createLineBorder(Color.BLACK)
        )
        threadSafe {
            panel.add(label)
        }
        return label
    }

    /**
     * Sets color of the label
     */
    static def setLabelColor(label, color) {
        threadSafe { label.setBackground(color) }
    }

    /**
     * Runs closure in Swing Event thread and repaints the panel. Sleeps after the change.
     */
    static def threadSafe(closure) {
        SwingUtilities.invokeAndWait {
            closure()
            panel.repaint()
        }
        Thread.sleep(1000 - speed.value)
    }

    /**
     * Splits a list of numbers in half
     */
    static def split(List<Integer> list) {
        int listSize = list.size()
        int middleIndex = listSize / 2
        def list1 = list[0..<middleIndex]
        def list2 = list[middleIndex..listSize - 1]
        return [list1, list2]
    }

    /**
     * Merges two sorted lists into one
     */
    static List<Integer> merge(label, List<Integer> a, List<Integer> b) {
        setLabelColor(label, threadColor())
        int i = 0, j = 0
        final int newSize = a.size() + b.size()
        List<Integer> result = new ArrayList<Integer>(newSize)

        while ((i < a.size()) && (j < b.size())) {
            if (a[i] <= b[j]) result << a[i++]
            else result << b[j++]
        }

        if (i < a.size()) result.addAll(a[i..-1])
        else result.addAll(b[j..-1])
        return result
    }

    /**
     * Returns color of current thread.
     */
    static def threadColor() {
        return threadColors[Integer.valueOf(Thread.currentThread().name[-1]) - 1]
    }

    /**
     * Finishes the task
     */
    static def finishTask(label, result) {
        threadSafe {
            label.text = result.toString()
            label.background = COLOR_FINISHED
        }
        return result
    }
}
S.panel.setLayout(null)

//Creates Number of threads slider
S.numThreads = S.swing.slider(value: 3, minimum: 1, maximum: 8, paintTicks: true, paintLabels: true, minorTickSpacing: 1)
S.numThreads.border = S.swing.titledBorder("""Number of threads ${S.numThreads.value}""")
S.numThreads.addChangeListener(new ChangeListener() {
    void stateChanged(ChangeEvent event) {
        event.source.border.title = """Number of threads ${event.source.value}"""
    }
})

//Creates Problem Size slider
S.problemSize = S.swing.slider(value: 32, minimum: 4, maximum: 64, paintTicks: true, paintLabels: true, minorTickSpacing: 1)
S.problemSize.border = BorderFactory.createTitledBorder("""Problem size ${S.problemSize.value}""")
S.problemSize.addChangeListener(new ChangeListener() {
    void stateChanged(ChangeEvent event) {
        event.source.border.title = """Problem size ${event.source.value}"""
    }
})

//Creates Speed slider
S.speed = S.swing.slider(minimum: 0, maximum: 1000, value: 700, border: BorderFactory.createTitledBorder("Speed"))

//Creates Start Button
S.startButton = S.swing.button(text: 'Start', actionPerformed: { event ->
    S.numThreads.enabled = false
    S.problemSize.enabled = false
    def ps = S.problemSize.value
    event.source.enabled = false;
    S.panel.preferredSize = [ps * S.COL_WIDTH, 7 * S.ROW_HEIGHT]
    S.panel.removeAll();
    Thread.start { runDemo() }
})
//Creates frame
S.swing.edt {
    frame(title: "Visualisation of merge sort using fork join",
            size: [1024, 640],
            show: true) {
        borderLayout()
        vbox(constraints: BL.NORTH) {
            hbox()
                    {
                        label("<html>\
                         <ol>\
                             <li>Thread takes a task from the queue. If the tasks is too big (longer than two elements) it is split to two smaller tasks</li>\
                             <li>Subtasks are placed to queue to be processed</li>\
                              <li>While the task waits for its subtasks to finish the thread goes to 1.</li>\
                             <li>When the subtasks are finished their results are merged.</li>\
                         </ol>\
                     </html>")
                    }
            panel()
                    {
                        label(text: "Waiting in queue", background: S.COLOR_SCHEDULED, opaque: true, border: emptyBorder(3, 3, 3, 3));
                        label(text: "Waiting for subtasks", background: S.COLOR_WAIT, opaque: true, border: emptyBorder(3, 3, 3, 3));
                        label(text: "Finished", background: S.COLOR_FINISHED, opaque: true, border: emptyBorder(3, 3, 3, 3));
                        S.threadColors.eachWithIndex { color, idx ->
                            label(text: "Thread ${idx + 1}", background: color, opaque: true, border: emptyBorder(3, 3, 3, 3));
                        }
                    }
            hbox()
                    {
                        widget(S.speed)
                        widget(S.numThreads)
                        widget(S.problemSize)
                    }
            hbox()
                    {
                        widget(S.startButton)
                    }

        }
        scrollPane(viewportView: S.panel, constraints: BL.CENTER)
    }.setExtendedState(JFrame.MAXIMIZED_BOTH)
}

/**
 * Fork/Join task for merge sort.
 * Extends RecursiveTask to perform divide-and-conquer sorting.
 */
class MergeSortTask extends RecursiveTask<List<Integer>> {
    final List<Integer> nums
    final int row
    final int column
    final label

    MergeSortTask(List<Integer> nums, int row, int column, label) {
        this.nums = nums
        this.row = row
        this.column = column
        this.label = label
    }

    @Override
    protected List<Integer> compute() {
        println "Thread ${Thread.currentThread().name[-1]}: Sorting $nums"
        S.setLabelColor(label, S.threadColor())
        switch (nums.size()) {
            case 0..1:
                return S.finishTask(label, nums)
            case 2:
                if (nums[0] <= nums[1]) {
                    return S.finishTask(label, nums)
                } else {
                    return S.finishTask(label, nums[-1..0])
                }
            default:
                def splitList = S.split(nums)
                def label1 = S.createLabel(row + 1, column, splitList[0])
                def label2 = S.createLabel(row + 1, column + splitList[0].size(), splitList[1])
                S.setLabelColor(label, S.COLOR_WAIT)
                
                // Fork the first subtask to run asynchronously
                MergeSortTask task1 = new MergeSortTask(splitList[0], row + 1, column, label1)
                task1.fork()
                
                // Run the second subtask directly on the current thread
                MergeSortTask task2 = new MergeSortTask(splitList[1], row + 1, column + splitList[0].size(), label2)
                def result2 = task2.invoke()
                
                // Wait for the forked task to complete and merge results
                def result1 = task1.join()
                return S.finishTask(label, S.merge(label, result2, result1))
        }
    }
}

/**
 * Executes the demo.
 * @return
 */
def runDemo() {
    final def numbers = new ArrayList(S.problemSize.value..1)
    def pool = new ForkJoinPool(S.numThreads.value)  //feel free to experiment with the number of fork/join threads in the pool
    try {
        def topLabel = S.createLabel(0, 0, numbers)
        def task = new MergeSortTask(numbers, 0, 0, topLabel)
        def sorted = pool.invoke(task)
        S.threadSafe {
            S.numThreads.enabled = true
            S.problemSize.enabled = true
            S.startButton.enabled = true
        }
        println """Sorted numbers: ${sorted}"""
    } finally {
        pool.shutdown()
    }
}