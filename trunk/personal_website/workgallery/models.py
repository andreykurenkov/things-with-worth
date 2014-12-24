from django.db import models

class WorkCategory(models.Model):
    name = models.CharField(max_length=25)
    priority = models.IntegerField()
    
    def __unicode__(self):
        return self.name
    
    def __str__(self):
        return self.name
    
class Project(models.Model):
    category = models.ForeignKey(WorkCategory)
    name = models.CharField(max_length=50)
    image = models.ImageField(upload_to = 'projects')
    votes = models.IntegerField()
    what = models.TextField()
    when = models.TextField()
    where = models.TextField()
    details = models.TextField()
    
    def __unicode__(self):
        return self.name
    
    def __str__(self):
        return self.name
    
class ProjectImages(models.Model):
    project = models.ForeignKey(Project)
    image = models.ImageField(upload_to = 'projectimages')
    name = models.CharField(max_length=25)
    caption = models.TextField()
    
    def __unicode__(self):
        return self.name
    
    def __str__(self):
        return self.name