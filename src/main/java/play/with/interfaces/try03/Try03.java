package play.with.interfaces.try03;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class MyObject {
    abstract void printFields();
}

class A extends MyObject {
    String field1; // Can not be null
    String field2; // Can not be null

    @Override
    void printFields(){
        System.out.println(field1);
        System.out.println(field2);
    }
	
	public String getField1(){
		return field1;
	}
	
	public String getField2(){
		return field2;
	}
	
	public void setField1(String field1){
		this.field1 = field1;
	}
	
	public void setField2(String field2){
		this.field2 = field2;
	}

}

class B extends MyObject {
    int field1; // Can not be 0
    int field2; // Can not be 0

    @Override
    void printFields(){
        System.out.println(field1);
        System.out.println(field2);
    }
	
	public int getField1(){
		return field1;
	}
	
	public int getField2(){
		return field2;
	}
	
	public void setField1(int field1){
		this.field1 = field1;
	}
	
	public void setField2(int field2){
		this.field2 = field2;
	}
}

class AB extends MyObject {
    A field1;
    B field2;

    @Override
    void printFields(){
        field1.printFields();
        field2.printFields();
    }
	
	public A getField1(){
		return field1;
	}
	
	public B getField2(){
		return field2;
	}
	
	public void setField1(A field1){
		this.field1 = field1;
	}
	
	public void setField2(B field2){
		this.field2 = field2;
	}
}

interface Validator<X extends MyObject> {
    boolean valid(X myObject);
    Map<String, List<String>> reason(X myObject);
}

class ValidatorA implements Validator<A> {
    
    @Override
    public boolean valid(A myObject) throws ClassCastException {
        boolean result = true;
        try {
            if(myObject.getField1() == null) {
                result = false;
            }
            if (myObject.getField2() == null) {
                result = false;
            }
        } catch (ClassCastException ex) {
            result = false;
            System.out.println("Not the right class type");
        }
        return result;
    }

    @Override
    public Map<String, List<String>> reason(A myObject) {
		Map<String, List<String>> reasons = new HashMap<>();
		List<String> errors = new ArrayList<>();
        try {
            if(myObject.getField1() == null) {
                errors.add("field1 can't be null");
            }
            if (myObject.getField2() == null) {
                errors.add("field2 can't be null");
            }
            reasons.put("a", errors);

        } catch (ClassCastException ex) {
            System.out.println("Not the right class type");
        }
        return reasons;
    }
}

class ValidatorB implements Validator<B> {

    @Override
    public boolean valid(B myObject) throws ClassCastException {
        boolean result = true;

        try {
            if(myObject.getField1() == 0) {
                result = false;
            }
            if (myObject.getField2() == 0) {
                result = false;
            }
        } catch (ClassCastException ex) {
            result = false;
            System.out.println("Not the right class type");
        }
        return result;
    }

    @Override
    public Map<String, List<String>> reason(B myObject) {
		Map<String, List<String>> reasons = new HashMap<>();
		List<String> errors = new ArrayList<>();
		try {
            if(myObject.getField1() == 0) {
                errors.add("field1 can't be 0");
            }
            if (myObject.getField2() == 0) {
                errors.add("field2 can't be 0");
            }
            reasons.put("b", errors);
        } catch (ClassCastException ex) {
            System.out.println("Not the right class type");
        }
        return reasons;
    }
}

class ValidatorAB implements Validator<AB> {
	private Validator<AB> validator;
	
	public ValidatorAB(Validator<AB> validator){
		this.validator = validator;
	}

    @Override
    public boolean valid(AB myObject) {
        return validator.valid(myObject);
    }

    @Override
    public Map<String, List<String>> reason(AB myObject) {
        return validator.reason(myObject);
    }
}

class ValidatorABValidOnlyA implements Validator<AB> {
	private ValidatorA validatorA;
	
	public ValidatorABValidOnlyA(ValidatorA validatorA){
		this.validatorA = validatorA;
	}

    @Override
    public boolean valid(AB myObject) {
        boolean result = true;
        try {
            A a = myObject.getField1();
            return validatorA.valid(a);
        } catch (ClassCastException ex) {
            System.out.println("Not the right class type");
            result = false;
        }
        return result;
    }

    @Override
    public Map<String, List<String>> reason(AB myObject) {
		Map<String, List<String>> reasons = new HashMap<>();
		try {
            A a = myObject.getField1();
            reasons = validatorA.reason(a);
        } catch (ClassCastException ex) {
            System.out.println("Not the right class type");
        }
        return reasons;
    }
}

class ValidatorABValidOnlyB implements Validator<AB> {
	private ValidatorB validatorB;
	
	public ValidatorABValidOnlyB(ValidatorB validatorB){
		this.validatorB = validatorB;
	}

    @Override
    public boolean valid(AB myObject) {
        boolean result = true;
        try {
            B b = myObject.getField2();
            return validatorB.valid(b);
        } catch (ClassCastException ex) {
            System.out.println("Not the right class type");
            result = false;
        }
        return result;
    }

    @Override
    public Map<String, List<String>> reason(AB myObject) {
		Map<String, List<String>> reasons = new HashMap<>();
		try {
            B b = myObject.getField2();
            reasons = validatorB.reason(b);
        } catch (ClassCastException ex) {
            System.out.println("Not the right class type");
        }
        return reasons;
    }
}

class ValidatorABValidBothAB implements Validator<AB> {
	private ValidatorA validatorA;
	private ValidatorB validatorB;
	
	public ValidatorABValidBothAB(ValidatorA validatorA, ValidatorB validatorB){
		this.validatorA = validatorA;
		this.validatorB = validatorB;
	}

    @Override
    public boolean valid(AB myObject) {
        boolean result = true;
        try {
            A a = myObject.getField1();
            B b = myObject.getField2();
			return validatorA.valid(a) && validatorB.valid(b);

        } catch (ClassCastException ex) {
            System.out.println("Not the right class type");
            result = false;
        }

        return result;
    }

    @Override
    public Map<String, List<String>> reason(AB myObject) {
		Map<String, List<String>> reasons = new HashMap<>();
		try {
            A a = myObject.getField1();
            B b = myObject.getField2();
            reasons = validatorA.reason(a);
            reasons.putAll(validatorB.reason(b));
        } catch (ClassCastException ex) {
            System.out.println("Not the right class type");
        }
        return reasons;
    }
}

public class Try03 {

    public Try03() {

        A a = new A();
        a.setField1("field1");

        ValidatorA validatorA = new ValidatorA();
        if(!validatorA.valid(a)) {
            System.out.println(validatorA.reason(a));
        }

        B b = new B();
        b.setField1(2);

        ValidatorB validatorB = new ValidatorB();
        if(!validatorB.valid(b)) {
            System.out.println(validatorB.reason(b));
        }

        AB abA = new AB();
        abA.setField1(new A());
        abA.setField2(new B());

        ValidatorABValidOnlyA validatorABValidOnlyA = new ValidatorABValidOnlyA(validatorA);
        ValidatorAB validatorAB = new ValidatorAB(validatorABValidOnlyA);
        if(!validatorAB.valid(abA)) {
            System.out.println(validatorAB.reason(abA));
        }


        AB abB = new AB();
        abB.setField1(new A());
        abB.setField2(new B());

        ValidatorABValidOnlyB validatorABValidOnlyB = new ValidatorABValidOnlyB(validatorB);
        validatorAB = new ValidatorAB(validatorABValidOnlyB);
        if(!validatorAB.valid(abB)) {
            System.out.println(validatorAB.reason(abB));
        }

        AB abAB = new AB();
        abAB.setField1(new A());
        abAB.setField2(new B());

        ValidatorABValidBothAB validBothAB = new ValidatorABValidBothAB(validatorA, validatorB);
        validatorAB = new ValidatorAB(validBothAB);
        if(!validatorAB.valid(abAB)) {
            System.out.println(validatorAB.reason(abAB));
        }
    }
}
