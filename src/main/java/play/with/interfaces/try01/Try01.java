package play.with.interfaces.try01;


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

interface Validator {
    boolean valid(MyObject myObject);
    Map<String, List<String>> reason(MyObject myObject);
}

class ValidatorA implements Validator {
    
    @Override
    public boolean valid(MyObject myObject) throws ClassCastException {
        boolean result = true;
        try {
            A a = (A) myObject;
            if(a.getField1() == null) {
                result = false;
            }
            if (a.getField2() == null) {
                result = false;
            }
        } catch (ClassCastException ex) {
            result = false;
            System.out.println("Not the right class type");
        }
        return result;
    }

    @Override
    public Map<String, List<String>> reason(MyObject myObject) {
		Map<String, List<String>> reasons = new HashMap<>();
		List<String> errors = new ArrayList<>();
        try {
            A a = (A) myObject;
            if(a.getField1() == null) {
                errors.add("field1 can't be null");
            }
            if (a.getField2() == null) {
                errors.add("field2 can't be null");
            }
            reasons.put("a", errors);
        } catch (ClassCastException ex) {
            System.out.println("Not the right class type");
        }
        return reasons;
    }
}

class ValidatorB implements Validator {

    @Override
    public boolean valid(MyObject myObject) throws ClassCastException {
        boolean result = true;

        try {
            B b = (B) myObject;
            if(b.getField1() == 0) {
                result = false;
            }
            if (b.getField2() == 0) {
                result = false;
            }
        } catch (ClassCastException ex) {
            result = false;
            System.out.println("Not the right class type");
        }
        return result;
    }

    @Override
    public Map<String, List<String>> reason(MyObject myObject) {
		Map<String, List<String>> reasons = new HashMap<>();
		List<String> errors = new ArrayList<>();
		try {
            B b = (B) myObject;
            if(b.getField1() == 0) {
                errors.add("field1 can't be 0");
            }
            if (b.getField2() == 0) {
                errors.add("field2 can't be 0");
            }
            reasons.put("b", errors);
        } catch (ClassCastException ex) {
            System.out.println("Not the right class type");
        }
        return reasons;
    }
}

class ValidatorABValidOnlyA implements Validator {
	private ValidatorA validatorA;
	
	public ValidatorABValidOnlyA(ValidatorA validatorA){
		this.validatorA = validatorA;
	}

    @Override
    public boolean valid(MyObject myObject) {
        boolean result = true;
        try {
            AB ab = (AB) myObject;
            A a = ab.getField1();
            return validatorA.valid(a);
        } catch (ClassCastException ex) {
            System.out.println("Not the right class type");
            result = false;
        }
        return result;
    }

    @Override
    public Map<String, List<String>> reason(MyObject myObject) {
		Map<String, List<String>> reasons = new HashMap<>();
		try {
            AB ab = (AB) myObject;
            A a = ab.getField1();
            reasons = validatorA.reason(a);
        } catch (ClassCastException ex) {
            System.out.println("Not the right class type");
        }
        return reasons;
    }
}

class ValidatorABValidOnlyB implements Validator {
	private ValidatorB validatorB;
	
	public ValidatorABValidOnlyB(ValidatorB validatorB){
		this.validatorB = validatorB;
	}

    @Override
    public boolean valid(MyObject myObject) {
        boolean result = true;
        try {
            AB ab = (AB) myObject;
            B b = ab.getField2();
            return validatorB.valid(b);
        } catch (ClassCastException ex) {
            System.out.println("Not the right class type");
            result = false;
        }
        return result;
    }

    @Override
    public Map<String, List<String>> reason(MyObject myObject) {
		Map<String, List<String>> reasons = new HashMap<>();
		try {
            AB ab = (AB) myObject;
            B b = ab.getField2();
            reasons = validatorB.reason(b);
        } catch (ClassCastException ex) {
            System.out.println("Not the right class type");
        }
        return reasons;
    }
}

class ValidatorABValidBothAB implements Validator {
	private ValidatorA validatorA;
	private ValidatorB validatorB;
	
	public ValidatorABValidBothAB(ValidatorA validatorA, ValidatorB validatorB){
		this.validatorA = validatorA;
		this.validatorB = validatorB;
	}

    @Override
    public boolean valid(MyObject myObject) {
        boolean result = true;

        try {
            AB ab = (AB) myObject;
            A a = ab.getField1();
            B b = ab.getField2();
			return validatorA.valid(a) && validatorB.valid(b);

        } catch (ClassCastException ex) {
            System.out.println("Not the right class type");
            result = false;
        }

        return result;
    }

    @Override
    public Map<String, List<String>> reason(MyObject myObject) {
		Map<String, List<String>> reasons = new HashMap<>();
		try {
            AB ab = (AB) myObject;
            A a = ab.getField1();
            B b = ab.getField2();
            reasons = validatorA.reason(a);
            reasons.putAll(validatorB.reason(b));
        } catch (ClassCastException ex) {
            System.out.println("Not the right class type");
        }
        return reasons;
    }
}
/*
    Is there any better way to avoid 273, I want to pass only one validator.
*/

public class Try01 {

    public Try01() {

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
        if(!validatorABValidOnlyA.valid(abA)) {
            System.out.println(validatorABValidOnlyA.reason(abA));
        }


        AB abB = new AB();
        abB.setField1(new A());
        abB.setField2(new B());

        ValidatorABValidOnlyB validatorABValidOnlyB = new ValidatorABValidOnlyB(validatorB);
        if(!validatorABValidOnlyB.valid(abB)) {
            System.out.println(validatorABValidOnlyB.reason(abB));
        }

        AB abA3 = new AB();
        abA3.setField1(new A());
        abA3.setField2(new B());

        ValidatorABValidBothAB validBothAB = new ValidatorABValidBothAB(validatorA, validatorB);
        if(!validBothAB.valid(abA3)) {
            System.out.println(validBothAB.reason(abA3));
        }
    }
}
