package com.section.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MultipleSortingTest {
	public static class Employee{
		String name;
		int age;
		String mobile;
		
		public Employee(String name, int age, String mobile) {
			this.name = name;
			this.age = age;
			this.mobile = mobile;
		}

		@Override
		public String toString() {
			return "Employee [name=" + name + ", age=" + age + ", mobile=" + mobile + "]";
		}
		
	}
	public static class MultipleComparators implements Comparator<Employee>{
		List<Comparator<Employee>> comparators;
		
		public MultipleComparators(Comparator<Employee> ...comparators) {
			this.comparators = Arrays.asList(comparators);
			
		}
		
		@Override
		public int compare(Employee o1, Employee o2) {
			for(Comparator<Employee> comp: this.comparators) {
				int res = comp.compare(o1, o2);
				if(res != 0)
					return res;
			}
			return 0;
		}
		
	}
	
	public static Comparator<Employee> NAME_ASC = new Comparator<MultipleSortingTest.Employee>() {
		@Override
		public int compare(Employee o1, Employee o2) {
			
			return o1.name.compareTo(o2.name);
		}
	};
	
	public static Comparator<Employee> NAME_DSC = new Comparator<MultipleSortingTest.Employee>() {
		@Override
		public int compare(Employee o1, Employee o2) {
			
			return o2.name.compareTo(o1.name);
		}
	};
	
	public static Comparator<Employee> AGE_ASC = new Comparator<MultipleSortingTest.Employee>() {
		@Override
		public int compare(Employee o1, Employee o2) {
			
			return o1.age - o2.age;
		}
	};
	
	public static Comparator<Employee> AGE_DSC = new Comparator<MultipleSortingTest.Employee>() {
		@Override
		public int compare(Employee o1, Employee o2) {
			
			return o2.age - o1.age;
		}
	};
	
	public static void main(String[] args) {
		List<Employee> employess = Arrays.asList(
				new Employee("Chirag", 24, "1234567"),
				new Employee("Chirag", 25, "1234567"),
				new Employee("Rishabh", 25, "585558"),
				new Employee("Rishabh", 26, "585558"),
				new Employee("Alind", 30, "986868")
			);
		
		Collections.sort(employess, new MultipleComparators(NAME_ASC));
		System.out.println("Name ASC: " + employess);
		
		Collections.sort(employess, new MultipleComparators(NAME_DSC));
		System.out.println("Name DSC: " + employess);
		
		Collections.sort(employess, new MultipleComparators(NAME_DSC, AGE_DSC));
		System.out.println("Name DSC, Age DSC: " + employess);
		
	}

}
