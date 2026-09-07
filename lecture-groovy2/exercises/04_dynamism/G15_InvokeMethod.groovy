List<Employee> testEmployees = [
        new Employee("John", 29),
        new Employee("Martin", 31),
        new Employee("Fred", 42),
        new Employee("George", 23),
        new Employee("Mike", 51),
        new Employee("Lisa", 27),
        new Employee("Stefan", 36)
]

Company company = new Company(employees: testEmployees)

println "Senior employees ${company.findAllSeniorEmployees().name}"
println "Expert employees ${company.findAllExperiencedEmployees().name}"

println "Junior employees ${company.findAllJuniorEmployees().name}"
println "Fresh employees  ${company.findAllFreshEmployees().name}"

//TASK Describe how the dynamic queries work
//TASK Enable search for employees with names starting with the letter 'M'
//println "M-employees ${company.findAllMEmployees()*.name}"


class Company {
    List<Employee> employees = []

    public Object invokeMethod(String methodName, Object o) {
        def matcher = methodName =~ 'findAll(.*)Employees'
        if (matcher.matches()) {
            String action = matcher.group(1)
            if (action.toLowerCase() in ['senior', 'experienced', 'expert']) {
                return employees.findAll {it.age > 30}
            }
            if (action.toLowerCase() in ['junior', 'fresh']) {
                return employees.findAll {it.age <= 30}
            }
        }

        return [];
    }
}


public enum Category {
    EMPLOYEE, MANAGER, OWNER
}

public class Employee {
    private String id = null;
    private int age;
    private Category category = Category.EMPLOYEE;
    private Department department = null;
    private BigDecimal pay = BigDecimal.ZERO;
    private Date startTime = new Date();
    private String name;
    private Set<String> technologies = new HashSet<String>();

    public Employee() {
    }

    public Employee(final String name, final int age) {
        this.age = age;
        this.name = name;
    }

    public Employee(final String id, final String name, final int age, final Category category, final Department department, final BigDecimal pay, final Date startTime) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.category = category;
        this.department = department;
        this.pay = pay;
        this.startTime = startTime;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public Department getDepartment() {
        return department;
    }

    public BigDecimal getPay() {
        return pay;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Set<String> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(final Set<String> technologies) {
        this.technologies = technologies;
    }
}

class Department {
    String name

    Department(String name) {
        this.name = name
    }

    public String toString() {
        return name
    }
}