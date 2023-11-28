import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

dataFrame = pd.read_csv('input_dop.csv', sep=';')

pd.to_numeric(dataFrame['open'])
pd.to_numeric(dataFrame['max'])
pd.to_numeric(dataFrame['min'])
pd.to_numeric(dataFrame['close'])

# преобразование данных в стобцы
dataFrame = pd.melt(dataFrame, id_vars=['date'], value_vars=['open', 'max', 'min', 'close'])

sns.boxplot(x='date',  y='value', data=dataFrame, hue='variable')
plt.legend()
plt.show()
