import os
folder = 'negative'
with open("negatives.txt",'w') as writeTo:
	for file in os.listdir("./"+folder):
		writeTo.write(folder+'\\'+file+'\n')
		print folder+'\\'+file