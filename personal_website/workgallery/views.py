from django.shortcuts import get_object_or_404, render_to_response
from django.http import HttpResponseRedirect, HttpResponse
from workgallery.models import WorkCategory, Project
from django.template.context import RequestContext

def gallery(request):
    post = request.POST.copy()
    categories = WorkCategory.objects.all().order_by('-priority').reverse()
    mainCategoryName = categories[0].name
    if request.is_ajax():
        print("asdfgjasdkafsdf")
        mainCategoryName = post['category']
    mainCategory = [category for category in categories if category.name==mainCategoryName][0]
    projects = mainCategory.project_set.all()
    currentProjectName = projects[0].name
    if request.is_ajax():
        currentProjectName = post['project']
    project = [project for project in projects if project.name==currentProjectName][0]
    context = {'mainCategory':mainCategory,'categories': categories,'project':project,'projects': projects}
    return render_to_response('workgallery/workgallery.html',context_instance=RequestContext(request, context))
