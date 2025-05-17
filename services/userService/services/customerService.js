let customers = [];

exports.getAll = () => customers;

exports.add = (data) => {
  const id = customers.length + 1;
  const customer = { customerId: id, ...data };
  customers.push(customer);
  return customer;
};

exports.update = (id, data) => {
  const index = customers.findIndex(c => c.customerId === id);
  if (index >= 0) {
    customers[index] = { ...customers[index], ...data };
    return customers[index];
  }
  return null;
};

exports.remove = (id) => {
  const lengthBefore = customers.length;
  customers = customers.filter(c => c.customerId !== id);
  return customers.length < lengthBefore;
};
