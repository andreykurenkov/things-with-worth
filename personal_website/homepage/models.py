from django.db import models

class homepage(models.Model):
    text = models.TextField()
    last_updated = models.DateTimeField('Last Updated')
    verticalOne = models.ImageField()
    verticalTwo = models.ImageField()
    horizontalOne = models.ImageField()
    horizontalTwo = models.ImageField()
    horizontalThree = models.ImageField()
    
    def __unicode__(self):
        return self.text
    
    def __str__(self):
        return self.text