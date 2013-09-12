file=open('triangle.txt','r')
lines=[]
for line in file:
	nums=line.split(' ')
	intNums=[]
	for num in nums:
		intNums.append(int(num))
	lines.append(intNums)
numLines=len(lines)
maxTo=[0]*(numLines+1)
for line in range(numLines):
	nums=lines[numLines-(line+1)]
	for num in range(len(nums)):
		maxTo[num]=nums[num]+max(maxTo[num],maxTo[num+1])
print maxTo[0]
	
