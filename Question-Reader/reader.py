import pyttsx3
import time
engine = pyttsx3.init()
engine.setProperty('rate', 120)

quizletList = []
file = open('Question-Reader\quiz2.txt', 'r')
copyList = file.readlines()
file.close()
for i in range(0, len(copyList), 2):
    quizletList.append([copyList[i], copyList[i+1]])

for term in quizletList:
    engine.say(str(quizletList[quizletList.index(term)][0]))
    engine.runAndWait()
    q = input('Answer? (first part only)')
    type(q)
    correct = True
    for i in range(len(q)):
        if q[i] == quizletList[quizletList.index(term)][1][i]:
            correct = True
        else:
            correct = False
            engine.say('incorrect')
            engine.runAndWait()
            break
    engine.say(str(quizletList[quizletList.index(term)][1]))
    engine.runAndWait()
    query = input("Continue? (y/n) ")
    type(query)
    if query == 'n':
        break

print(len(quizletList))