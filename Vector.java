package uob.oop;

public class Vector {
    private double[] doubElements;

    public Vector(double[] _elements) {
        //TODO Task 3.1 - 0.5 marks
        doubElements = _elements;
    }

    public double getElementAtIndex(int _index) {
        //TODO Task 3.2 - 2 marks
        if (_index >= doubElements.length){
            return -1;
        } else {
            return doubElements[_index]; //you need to modify the return value
        }
    }

    public void setElementAtIndex(double _value, int _index) {
        //TODO Task 3.3 - 2 marks
        if (_index >= doubElements.length){
            doubElements[doubElements.length - 1] = _value;
        } else {
            doubElements[_index] = _value;
        }
    }

    public double[] getAllElements() {
        //TODO Task 3.4 - 0.5 marks
        return doubElements; //you need to modify the return value
    }

    public int getVectorSize() {
        //TODO Task 3.5 - 0.5 marks
        return doubElements.length; //you need to modify the return value
    }

    public Vector reSize(int _size) {
        //TODO Task 3.6 - 6 marks
        if (_size == this.getVectorSize() || _size <= 0){
            return this;
        } else {
            double[] tempArray = new double[_size];
            int copyLength = Math.min(this.getVectorSize(), _size);
            for (int i = 0; i < copyLength; i++){
                tempArray[i] = this.getElementAtIndex(i);
            }
            for (int i = copyLength; i < _size; i++){
                tempArray[i] = -1;
            }
            return new Vector(tempArray); //you need to modify the return value
        }

    }

    public Vector add(Vector _v) {
        //TODO Task 3.7 - 2 marks
        int newSize = Math.max(this.getVectorSize(), _v.getVectorSize());
        Vector tempThis = this.reSize(newSize);
        Vector tempV = _v.reSize(newSize);
        double[] tempArray = new double[newSize];
        for (int i = 0; i < newSize; i++) {
            tempArray[i] = tempThis.getElementAtIndex(i) + tempV.getElementAtIndex(i);
        }
        return new Vector(tempArray); //you need to modify the return value
    }

    public Vector subtraction(Vector _v) {
        int newSize = Math.max(this.getVectorSize(), _v.getVectorSize());
        Vector tempThis = this.reSize(newSize);
        Vector tempV = _v.reSize(newSize);
        double[] tempArray = new double[newSize];
        for (int i = 0; i < newSize; i++) {
            tempArray[i] = tempThis.getElementAtIndex(i) - tempV.getElementAtIndex(i);
        }
        return new Vector(tempArray); //you need to modify the return value
    }

    public double dotProduct(Vector _v) {
        //TODO Task 3.9 - 2 marks
        int newSize = Math.max(this.getVectorSize(), _v.getVectorSize());
        Vector tempThis = this.reSize(newSize);
        Vector tempV = _v.reSize(newSize);
        Double result = 0.0;
        for (int i = 0; i < newSize; i++) {
            result += tempV.getElementAtIndex(i) * tempThis.getElementAtIndex(i);
        }
        return result; //you need to modify the return value
    }


    public double magnitude(){
        //Extra Method for finding magnitude of vector
        double result = 0.0;
        for (int i = 0; i < this.getVectorSize(); i++){
            result += Math.pow(this.getElementAtIndex(i), 2);
        }
        result = Math.sqrt(result);
        return result;
    }

    public double cosineSimilarity(Vector _v) {
        //TODO Task 3.10 - 6.5 marks
        int newSize = Math.max(this.getVectorSize(), _v.getVectorSize());
        Vector tempThis = this.reSize(newSize);
        Vector tempV = _v.reSize(newSize);
        double absValueThis = tempThis.magnitude();
        double absValueV = tempV.magnitude();
        return (tempThis.dotProduct(tempV))/(absValueV * absValueThis); //you need to modify the return value
    }

    @Override
    public boolean equals(Object _obj) {
        Vector v = (Vector) _obj;
        boolean boolEquals = true;

        if (this.getVectorSize() != v.getVectorSize())
            return false;

        for (int i = 0; i < this.getVectorSize(); i++) {
            if (this.getElementAtIndex(i) != v.getElementAtIndex(i)) {
                boolEquals = false;
                break;
            }
        }
        return boolEquals;
    }

    @Override
    public String toString() {
        StringBuilder mySB = new StringBuilder();
        for (int i = 0; i < this.getVectorSize(); i++) {
            mySB.append(String.format("%.5f", doubElements[i])).append(",");
        }
        mySB.delete(mySB.length() - 1, mySB.length());
        return mySB.toString();
    }
}
