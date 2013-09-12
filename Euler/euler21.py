import math
def divisorGenerator(n):
	list=[]
	for i in xrange(1,int(math.sqrt(n)+1)):
		if n%i == 0:
			list.append(i)
			list.append(n/i)
	return list

sum=0
divisorSums=[0]*10000
for i in range(1,10000):
	print divisorGenerator(i)
	divSum=sum(divisorGenerator(i))
	divisorSums[i]=divSum
	if divSum<10000 and divisorsSums[divSum]==divisorSums[i]:
		if divSum<i:
			sum+=divisorSums[i]+divisorsSums[divSum]
print sum

