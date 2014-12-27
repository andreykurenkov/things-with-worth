from django.conf.urls import patterns, include, url
from django.conf import settings
from django.contrib import admin
from homepage import views as hviews
from workgallery import views as wviews
from django.conf.urls.static import static

admin.autodiscover()

urlpatterns = patterns('',
	url(r'^$', hviews.index),
	url(r'^gallery/$', wviews.gallery),	
    url(r'^writing/', include('zinnia.urls')),
    url(r'^comments/', include('django.contrib.comments.urls')),
    url(r'^admin/', include(admin.site.urls)),
)

if settings.DEBUG:
	urlpatterns += static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)
	urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)