<h1>四則演算プログラム</h1>

<div align="right">2017-02-06</div>

プログラムの目的と概要
----------------------

このプログラムは、通常の数式（8桁までの整数値の四則演算のみ、括弧も使え
るようにする）を標準入力から読み取り、計算結果を標準出力に書き出しす
コマンドラインのプログラムです。入力した数式に誤り等があり計算ができ
ない場合、標準エラー出力にエラーメッセージを表示します。このプログラム
のソースコードはJava 言語で記述されています。

プログラムの仕様
----------------

◎計算できる桁数

このプログラムで扱える数値は８桁の整数値です。つまり、-99999999 から
99999999 までの負数を含む整数です。数式の計算結果および中間計算結果
に関しても８桁の整数の範囲までとします。計算結果が８桁に収まらない
場合はエラーメッセージを標準エラーに出力します。

◎四則演算とその表記

四則演算を、以下の４つの記号を二項演算子として表現します。

    加算: +
    減算: -
    乗算: *
    除算: /

除算が整数で割り切れない場合、小数点以下を切り捨てます。
四則演算の優先順は数式の右側よりも左側が先です。
ただし、加算・減算よりも乗算・除算を優先して計算します。

例えば以下の数式例:

    1 + 3 * 4 / 5

を計算する場合、3 * 4 の結果得られる12  を 5 で割り 2 を得、
1 + 2 を計算することにより最終結果として 3 を得ます。

を計算して結果は

◎括弧と優先順位

 丸括弧により四則演算の優先順位を上げることができます。

例えば以下の数式例:

    (1 + 3) * 4

では、1 + 3 の計算が先に実行されるため、計算結果は 16
となります。

プログラムの制限事項
--------------------

◎マイナス符号付き整数値について

入力整数値にマイナス符号を付けて

    -123

のように表現します。以下の数式例:

    124 + -123

の演算結果は 1 です。このマイナス符号は数値リテラルの
一部であって単項演算子ではありません。したがって下記の
ような数式は処理せずエラーメッセージを表示します。

    124 + -(-123)

プログラムのビルド方法
----------------------

    JDK8
    Ant 1.9

をインストールした環境で送付のアーカイブを展開し、
コマンドラインから ant を実行するとビルド・テスト
を実施した後に JAR ファイルが生成されます。

NetBeans IDE をインストールしている環境であれば
NetBeans でもビルド等ができます。

プログラムの使用方法
--------------------

ビルド後にコマンドラインから

    java -jar dist/EvalExpr.jar

のように起動すると標準入力から数式を入力できるようになります。
数式は改行で終端しますので改行を入力するごとに計算結果が標準
出力に表示されます。プログラムを終了するには標準入力から
文字列 QUIT と改行コードを入力します。

以下は使用例です。

    % java -jar dist/EvalExpr.jar
    1 + 3<改行>
    4
    2 *<改行>
    Error: syntax error: operator * lacks one operand. check the left hand and right hand.
    2 * (3 + 2)<改行>
    10
    QUIT<改行>

    % echo '1 + 3' | java -jar dist/EvalExpr.jar
    4