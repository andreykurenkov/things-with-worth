import os.dir

with writeTo as open("positive.txt",'w'):
	for file in os.dir.listdir("."):
		writeTo.write(file)
	