import math

done=False
at=1
while not done:
	num=at*(at+1)/2
	at+=1
	divising=0
	for n in range(1,int(round(math.sqrt(num)))):
		if num%n==0:
			divising+=2
		if divising>500:
			done=True
print num