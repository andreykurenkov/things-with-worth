maxStore=1000000
memory=[0]*maxStore
memory[1]=1

def recurse(startNum):
	if startNum==1:
		return 1
	if startNum<maxStore and memory[startNum]!=0:
		return memory[startNum]
	num=recurse(startNum/2 if startNum%2==0 else startNum*3+1)+1
	if startNum<maxStore:
		memory[startNum]=num
	return num
	
startNum=2
max=1
maxStart=1

for startNum in range(1,1000000):
	chain=recurse(startNum)
	if chain>max:
		max=chain
		maxStart=startNum
print maxStart