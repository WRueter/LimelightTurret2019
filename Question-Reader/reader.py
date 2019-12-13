import pyttsx3
engine = pyttsx3.init()
engine.setProperty('rate', 120)

quizletList = []
file = open('Question-Reader\quizlet.txt', 'r')
quizletList = file.readlines()
file.close()

for term in quizletList:
    engine.say(quizletList[quizletList.index(term)])
    engine.runAndWait()

print(len(quizletList))