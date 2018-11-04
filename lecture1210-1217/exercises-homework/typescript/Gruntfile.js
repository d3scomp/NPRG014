module.exports = function (grunt) {
    // load Grunt plugins from NPM
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-ts');
    grunt.loadNpmTasks('grunt-mkdir');

    // configure plugins
    grunt.initConfig({
        mkdir: {
            dist: {
                options: {
                    create: ['bin']
                }
            }
        },

        copy: {
            html: {
                expand: true,
                cwd: 'html/',
                src: '**/*',
                dest: 'bin/'
            },
        },

        ts: {
            default: {
                src: ['src/**/*.ts'],
                outDir: 'bin'
            }
        },

        watch: {
            ts: {
                files: ['src/**/*.ts'],
                tasks: ['ts']
            }
        }
    });

    // define tasks
    grunt.registerTask('default', ['compile', 'watch']);
    grunt.registerTask('compile', ['mkdir:dist', 'ts', 'copy:html']);
};