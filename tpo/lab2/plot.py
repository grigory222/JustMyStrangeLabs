import csv
import matplotlib.pyplot as plt

xs, ys = [], []
with open("function.csv", newline="", encoding="utf-8") as f:
    reader = csv.reader(f)
    next(reader)  # header
    for row in reader:
        if row[1].strip() == "undefined":
            continue
        xs.append(float(row[0]))
        ys.append(float(row[1]))

plt.figure(figsize=(12, 6))
plt.scatter(xs, ys, s=3, color="steelblue")
plt.axhline(0, color="black", linewidth=0.8)
plt.axvline(0, color="black", linewidth=0.8)
plt.ylim(-20, 20)
plt.xlabel("x")
plt.ylabel("f(x)")
plt.title("Function System")
plt.grid(True, alpha=0.3)
plt.tight_layout()
plt.savefig("function.png", dpi=150)
plt.show()
print("Сохранено: function.png")
