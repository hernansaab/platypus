import subprocess
import sys
import os
import setup_util
from os.path import expanduser

home = expanduser("~")

def start(args, logfile, errfile):
  print("----point1----")
  setup_util.replace_text("/home/solr/benchmarks/platypus/src/main/resources/application.conf", "db.default.host=shopping", "db.default.host=" + args.database_host)
  print("----point2----")
  setup_util.replace_text("/home/solr/benchmarks/platypus/src/main/resources/application.conf", "db.default.user=root", "db.default.user=benchmarkdbuser")
  print("----point4----")
  setup_util.replace_text("/home/solr/benchmarks/platypus/src/main/resources/application.conf", "db.default.password=myroot", "db.default.password=benchmarkdbpass")

  try:
    os.chdir("/home/solr/benchmarks/platypus")
    subprocess.check_call("sbt runPlatypus", shell=True, stderr=errfile, stdout=logfile)
    print("the platypus has been invoked")
    return 0
  except subprocess.CalledProcessError:
    return 1

def stop(logfile, errfile):
  try:
    os.chdir("/home/solr/benchmarks/platypus")
    subprocess.check_call("cd platypus && sbt stopPlatypus", shell=True, stderr=errfile, stdout=logfile)
    return 0
  except subprocess.CalledProcessError:
    return 1
