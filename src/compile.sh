echo "compiling java..."
javac linkstate.java
echo "done compiling..."
echo "setting up local run..."
java linkstate networktest.txt
