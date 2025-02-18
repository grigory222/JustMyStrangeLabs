def gauss_pivot(A, b):
    n = len(A)
    det = 1
    
    for i in range(n):
        max_row = i
        for k in range(i + 1, n):
            if abs(A[k][i]) > abs(A[max_row][i]):
                max_row = k
        
        if A[max_row][i] == 0:
            print("Ошибка: Матрица вырожденная")
            return None, None, None, None
        
        A[i], A[max_row] = A[max_row], A[i]
        b[i], b[max_row] = b[max_row], b[i]
        det *= A[i][i] * (-1 if i != max_row else 1)
        
        for j in range(i + 1, n):
            factor = A[j][i] / A[i][i]
            for k in range(i, n):
                A[j][k] -= factor * A[i][k]
            b[j] -= factor * b[i]
    
    x = [0] * n
    for i in range(n - 1, -1, -1):
        sum_ax = sum(A[i][j] * x[j] for j in range(i + 1, n))
        x[i] = (b[i] - sum_ax) / A[i][i]
    
    return x, det, A, b

def residual_vector(A, b, x):
    if x is None:
        return None
    n = len(A)
    residuals = [sum(A[i][j] * x[j] for j in range(n)) - b[i] for i in range(n)]
    return residuals

def validate_matrix(A, b, n):
    if not (0 < n and n <= 20):
        print("Ошибка: Размерность матрицы должна быть в пределах 0 < n <= 20")
        return False
    if len(A) != n or len(b) != n:
        print("Ошибка: Некорректные размеры матрицы или вектора b")
        return False
    for row in A:
        if len(row) != n:
            print("Ошибка: Матрица должна быть квадратной")
            return False
    if any(all(value == 0 for value in row) for row in A):
        print("Ошибка: Матрица содержит нулевую строку (вырожденная)")
        return False
    return True

def read_matrix_from_file(filename):
    try:
        with open(filename, 'r') as f:
            n = int(f.readline().strip())
            A = []
            b = []
            for _ in range(n):
                line = list(map(float, f.readline().split()))
                if len(line) != n + 1:
                    print("Ошибка: Некорректное количество элементов в строке")
                    return None, None
                A.append(line[:-1])
                b.append(line[-1])
        if not validate_matrix(A, b, n):
            return None, None
        return A, b
    except Exception as e:
        print(f"Ошибка при чтении файла: {e}")
        return None, None

def get_matrix_input():
    try:
        n = int(input("Введите размерность матрицы (0 < n <= 20): "))
        if (n < 1 or n > 20):
            raise ValueError("0 < n <= 20")
        A = []
        b = []
        print("Введите коэффициенты при неизвестных и свободные члены в конце каждой строки:")
        for _ in range(n):
            line = list(map(float, input().split()))
            if len(line) != n + 1:
                print("Ошибка: Некорректное количество элементов в строке")
                return None, None
            A.append(line[:-1])
            b.append(line[-1])
        if not validate_matrix(A, b, n):
            return None, None
        return A, b
    except Exception as e:
        print(f"Ошибка ввода данных: {e}")
        return None, None

def main():
    while True:
        choice = input("Выберите способ ввода данных (1 - файл, 2 - клавиатура): ")
        if choice == '1':
            filename = input("Введите имя файла: ")
            A, b = read_matrix_from_file(filename)
        elif choice == '2':
            A, b = get_matrix_input()
        else:
            print("Ошибка: Некорректный выбор, попробуйте снова")
            continue

        if A is None or b is None:
            print("Попробуйте снова")
            continue

        x, det, A_triangular, b_new = gauss_pivot([row[:] for row in A], b[:])
        if x is None:
            print("Попробуйте снова")
            continue

        residuals = residual_vector(A, b, x)

        print("=====================================")
        print("Треугольная матрица:")
        for row, bv in zip(A_triangular, b_new):
            print(row + [bv])
        print("\nВектор неизвестных:")
        print(x)
        print("\nОпределитель матрицы:")
        print(det)
        print("\nВектор невязок:")
        print(residuals)

        print("=====================================")

        # Решение через библиотеки
        import numpy as np
        x_np = np.linalg.solve(np.array(A, dtype=float), np.array(b, dtype=float))
        det_np = np.linalg.det(np.array(A, dtype=float))
        print("\nРешение через numpy:")
        print(x_np)
        print("\nОпределитель через numpy:")
        print(det_np)

        print("\nЗапуск программы заново...\n")

if __name__ == "__main__":
    while True:
        main()
