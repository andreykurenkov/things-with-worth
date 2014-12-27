from django.shortcuts import get_object_or_404, render_to_response
from django.http import HttpResponseRedirect, HttpResponse
from workgallery.models import WorkCategory, Project
from django.template.context import RequestContext

def gallery(request):
    htmlName = 'workgallery/workgallery.html'
    categories = WorkCategory.objects.all().order_by('-priority').reverse()
    if request.is_ajax() and 'category' in request.GET:
        mainCategoryName = request.GET['category']
        htmlName = 'workgallery/projectlist.html'
    else:
        mainCategoryName = categories[0].name
    mainCategory = [category for category in categories if category.name==mainCategoryName][0]
    projects = mainCategory.project_set.all()
    currentProjectName = projects[0].name
    if request.is_ajax() and 'project' in request.GET:
        if request.GET['project']!='1':
            currentProjectName = request.GET['project']
        htmlName = 'workgallery/projectdetails.html'
    project = [project for project in projects if project.name==currentProjectName][0]
    context = {'mainCategory':mainCategory,'categories': categories,'project':project,'projects': projects}
    return render_to_response(htmlName,context_instance=RequestContext(request, context))
