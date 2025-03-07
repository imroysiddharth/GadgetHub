<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Bootstrap demo</title>
    <link rel="stylesheet" href="style.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
  </head>
  <body>
      
          <jsp:include page="header.jsp" />
      
    <div class="container-fluid mt-5 mb-5">
        <div class="table-responsive">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>Transaction ID</th>
                        <th>Product ID</th>
                        <th>Username</th>
                        <th>Address</th>
                        <th>Qty</th>
                        <th>Amount</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>1234562</td>
                        <td><a href="#"></a>1234562</td>
                        <td>Siddharth</td>
                        <td>Patna,Bihar</td>
                        <td>5</td>
                        <td>1000000</td>
                        <td>Shipped</td>
                    </tr>
                    <tr class="text-center">
                        <td colspan="7">No items available</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
      
      <jsp:include page="footer.jsp" />
      
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
  </body>
</html>