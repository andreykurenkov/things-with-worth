"""
Django settings for Portfolio project.

For more information on this file, see
https://docs.djangoproject.com/en/1.6/topics/settings/

For the full list of settings and their values, see
https://docs.djangoproject.com/en/1.6/ref/settings/
"""

# Build paths inside the project like this: os.path.join(BASE_DIR, ...)
import os
BASE_DIR = os.path.dirname(os.path.dirname(__file__))


# Quick-start development settings - unsuitable for production
# See https://docs.djangoproject.com/en/1.6/howto/deployment/checklist/

# SECURITY WARNING: keep the secret key used in production secret!
SECRET_KEY = '-sg(p^63v(zxkj^h&!x#l)kbp46bjp^hxpzle3a4-%0n%e+$$4'

# SECURITY WARNING: don't run with debug turned on in production!
DEBUG = True

TEMPLATE_DEBUG = True

ALLOWED_HOSTS = []


# Application definition

INSTALLED_APPS = (
  'django.contrib.auth',
  'django.contrib.admin',
  'django.contrib.sites',
  'django.contrib.comments',
  'django.contrib.sessions',
  'django.contrib.messages',
  'django.contrib.staticfiles',
  'django.contrib.contenttypes',
  'tagging',
  'mptt',
  'zinnia',
  "workgallery",
  "homepage",
)

MIDDLEWARE_CLASSES = (
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.common.CommonMiddleware',
    'django.middleware.csrf.CsrfViewMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
)

TEMPLATE_CONTEXT_PROCESSORS = (
  'django.contrib.auth.context_processors.auth',
  'django.core.context_processors.i18n',
  'django.core.context_processors.request',
  "django.core.context_processors.media",
  'zinnia.context_processors.version',  # Optional
)

ROOT_URLCONF = 'personal_website.urls'

WSGI_APPLICATION = 'personal_website.wsgi.application'

MEDIA_ROOT = os.path.join(BASE_DIR, 'media')
MEDIA_URL = '/media/'
STATIC_URL = '/static/'
SITE_ID="1"

# Database
# https://docs.djangoproject.com/en/1.6/ref/settings/#databases

DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.sqlite3',
        'NAME': os.path.join(BASE_DIR, 'db.sqlite3'),
    }
}

# Internationalization
# https://docs.djangoproject.com/en/1.6/topics/i18n/

LANGUAGE_CODE = 'en-us'

TIME_ZONE = 'UTC'

USE_I18N = True

USE_L10N = True

USE_TZ = True


# Static files (CSS, JavaScript, Images)
# https://docs.djangoproject.com/en/1.6/howto/static-files/
TEMPLATE_DIRS = (
    os.path.join(BASE_DIR,'templates') # Change this to your own directory.
)

STATICFILES_DIRS = (
    os.path.join(BASE_DIR, "static"),
	os.path.join(BASE_DIR, "static/css"),
	os.path.join(BASE_DIR, "static/images"),
)