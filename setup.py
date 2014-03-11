
import subprocess
import sys
import os
import setup_util
from os.path import expanduser

home = expanduser("~")

def start(args, logfile, errfile):
  setup_util.replace_text("platypus/src/resources/application.conf", "db.default.host=shopping", "db.default.host=" + args.database_host)
  try:
    subprocess.check_call("cd platypus && sbt runPlatypus", shell=True, stderr=errfile, stdout=logfile)
    return 0
  except subprocess.CalledProcessError:
    return 1
def stop(logfile, errfile):
  try:
    subprocess.check_call("cd platypus && sbt stopPlatypus", shell=True, stderr=errfile, stdout=logfile)
    return 0
  except subprocess.CalledProcessError:
    return 1