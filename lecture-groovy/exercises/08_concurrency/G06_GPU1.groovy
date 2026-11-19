#!/usr/bin/env groovy

/**
 * Minimal GPU computation in pure Groovy using Aparapi (OpenCL under the hood).
 * Requirements: Groovy installed and an OpenCL-capable GPU/driver.
 * Run:  groovy gpu_aparapi.groovy
 */

@Grab('com.aparapi:aparapi:3.0.0')
import com.aparapi.Kernel
import com.aparapi.Range

// Problem size (feel free to tweak)
int n = 1_000_000

// Input/output buffers must be primitive arrays
float[] a = new float[n]
float[] b = new float[n]
float[] c = new float[n]

// Initialize inputs
for (int i = 0; i < n; i++) {
    a[i] = (float) i+1
    b[i] = (float) (2 * i)
}

// Define a GPU kernel entirely in Groovy (compiled to OpenCL at runtime)
Kernel kernel = new Kernel() {
    /**
    * This method will be translated by the OpenCL C compiler to C, if available and the code only uses:
    * primitive arrays (no objects or dynamic allocation),
    * arithmetic, control flow, and built-in math functions.
    * Aparapi also takes care of movinng the arrays a, b and c between the CPU and the GPU memories.
    * If there are problems finding a GPU, the OpenCL library or compiling the code, the method is run as Java on CPU.
    */
    @Override
    void run() {
        int i = getGlobalId()
        c[i] = a[i] + b[i]
    }
}

// Execute on the best available device (GPU if present, otherwise CPU fallback)
kernel.execute(Range.create(n))

// Validate a couple of results on host
assert c[0] == a[0] + b[0]
assert c[123456] == a[123456] + b[123456]

println "Execution mode: ${kernel.getExecutionMode()}"   // Expect GPU if OpenCL device is available
println "Sample results: " + (0..<20).collect { c[it] }

kernel.dispose()
