import pandas as pd
import numpy as np
from scipy.optimize import leastsq
import matplotlib.pyplot as plt


def fitting(fun, ds, p0, plot=False):
    x = list(ds['n'])
    y = list(ds['m'])
    error = lambda p, x, y: fun(p, x) - y
    para = leastsq(error, p0, args=(x, y))

    if plot:
        y_fitted = fun(para[0], x)
        plt.plot(x, y, 'paleturquoise')
        plt.plot(x, y_fitted, 'lightseagreen')
        plt.show()

    return para[0]


def main():
    df = pd.read_csv('raw-data.csv', sep=', ')
    print(df)
    dataset0 = df[: int(100000/3/20)]
    dataset1 = df[int(100000/3/20): int(100000/3*2/20)]
    dataset2 = df[int(100000/3*2/20): ]

    powerFunc = lambda p, x: p[0] * x ** p[1]
    logFunc = lambda p, x: p * np.log2(x)
    nlogFunc = lambda p, x: p * x * np.log2(x)

    res = {"<33333": {}, "33333-66666": {}, ">= 66666": {}}

    res["<33333"]["power"] = fitting(powerFunc, dataset0, [0.1, 2])
    res["33333-66666"]["power"] = fitting(powerFunc, dataset1, [0.1, 2])
    res[">= 66666"]["power"] = fitting(powerFunc, dataset2, [0.1, 2])

    res["<33333"]["log"] = fitting(logFunc, dataset0, [0.1])
    res["33333-66666"]["log"] = fitting(logFunc, dataset1, [0.1])
    res[">= 66666"]["log"] = fitting(logFunc, dataset2, [0.1])

    res["<33333"]["nlog"] = fitting(nlogFunc, dataset0, [0.1])
    res["33333-66666"]["nlog"] = fitting(nlogFunc, dataset1, [0.1])
    res[">= 66666"]["nlog"] = fitting(nlogFunc, dataset2, [0.1])

    df_out = pd.DataFrame(res)
    df_out["<33333"] =  df_out["<33333"].apply(lambda x: [round(a, 3) for a in x])
    df_out["33333-66666"] =  df_out["33333-66666"].apply(lambda x: [round(a, 3) for a in x])
    df_out[">= 66666"] =  df_out[">= 66666"].apply(lambda x: [round(a, 3) for a in x])
    pd.set_option("display.max_rows", None, "display.max_columns", None)
    pd.options.display.float_format = '{:.2f}'.format
    print(df_out)

    x = list(df['n'])
    y = list(df['m'])
    y_fitted = nlogFunc(res["33333-66666"]["nlog"], x)
    plt.title("{}*n*log(n)".format(round(res["33333-66666"]["nlog"][0], 3)))
    plt.plot(x, y, 'paleturquoise', label='Original')
    plt.plot(x, y_fitted, 'lightseagreen', label='Fitted')
    plt.legend()
    plt.show()


if __name__=='__main__':
    main()

