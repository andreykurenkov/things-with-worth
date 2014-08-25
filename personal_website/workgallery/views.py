from django.shortcuts import get_object_or_404, render_to_response
from django.http import HttpResponseRedirect, HttpResponse
from models import WorkCategory, Project

def index(request):
    post = request.POST.copy()
    categories = WorkCategory.objects.all().order_by('-priority')
    mainCategoryName = categories[0].name
    mainCategory = categories[0]
    if request.is_ajax():
        mainCategoryName = post['category']
        mainCategory = categories.filter({'name':mainCategoryName})
    projects = mainCategory.project_set.all()
    currentProjectName = projects[0].name
    if request.is_ajax():
        currentProjectName = post['project']
        project = projects.filter({'name':mainCategoryName})
    return render_to_response('workgallery/index.html', {'mainCategory':mainCategory,'categories': categories})
