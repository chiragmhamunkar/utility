package com.section.test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReflectionTest {
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
	
	public static enum SortType{
		ASC, DSC;
	}
	
	public static class GenericStringComparator implements Comparator<Employee>{
		private String fieldName;
		private SortType sortType;
		
		public GenericStringComparator(String fieldName, SortType sortType) {
			this.fieldName = fieldName;
			this.sortType = sortType;
		}
		
		@Override
		public int compare(Employee o1, Employee o2) {
			String o1Value = getValueFrom(o1, fieldName);
			String o2Value = getValueFrom(o2, fieldName);
			if(SortType.ASC.equals(sortType))
				return o1Value.compareTo(o2Value);
			return o2Value.compareTo(o1Value);
		}
		
	}
	
	public static class GenericIntegerComparator implements Comparator<Employee>{
		private String fieldName;
		private SortType sortType;
		
		public GenericIntegerComparator(String fieldName, SortType sortType) {
			this.fieldName = fieldName;
			this.sortType = sortType;
		}
		
		@Override
		public int compare(Employee o1, Employee o2) {
			int o1Value = getValueFrom(o1, fieldName);
			int o2Value = getValueFrom(o2, fieldName);
			if(SortType.ASC.equals(sortType))
				return o1Value - o2Value;
			return o2Value - o1Value;
		}
	}
	
	public static<T> T getValueFrom(Employee employee, String fieldName) {
		Field nameField;
		try {
			nameField = Employee.class.getDeclaredField(fieldName);
			T name = (T) nameField.get(employee);
			return name;
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		
	}
	
	public static void main(String args[]) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		System.out.println("Hello ");
		List<Employee> employess = Arrays.asList(
				new Employee("Chirag", 24, "1234567"),
				new Employee("Chirag", 25, "1234567"),
				new Employee("Rishabh", 25, "585558"),
				new Employee("Rishabh", 26, "585558"),
				new Employee("Alind", 30, "986868")
			);
		System.out.println(employess);
		Collections.sort(employess, new GenericStringComparator("name", SortType.ASC));
		System.out.println(employess);
		
		Collections.sort(employess, new GenericStringComparator("name", SortType.DSC));
		System.out.println(employess);
		
		
		
		Collections.sort(employess, new MultipleComparators(new GenericStringComparator("name", SortType.ASC)));
		System.out.println("Name ASC: " + employess);
		
		Collections.sort(employess, new MultipleComparators(new GenericStringComparator("name", SortType.DSC)));
		System.out.println("Name DSC: " + employess);
		
		Collections.sort(employess, new MultipleComparators(new GenericStringComparator("name", SortType.DSC), new GenericIntegerComparator("age", SortType.DSC)));
		System.out.println("Name DSC, Age DSC: " + employess);
		
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

}
